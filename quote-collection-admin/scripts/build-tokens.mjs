import { readFile, writeFile, mkdir } from 'node:fs/promises'

const source = new URL('../design/admin.tokens.json', import.meta.url)
const output = new URL('../src/styles/tokens.css', import.meta.url)
const document = JSON.parse(await readFile(source, 'utf8'))
const variables = new Map()

function resolveValue(token, seen = new Set()) {
  if (!token || typeof token !== 'object') throw new Error('Invalid token reference')
  if (typeof token.ref === 'string') {
    if (seen.has(token.ref)) throw new Error(`Circular token reference: ${token.ref}`)
    seen.add(token.ref)
    const target = token.ref.split('.').reduce((node, key) =>
      node && Object.hasOwn(node, key) ? node[key] : undefined, document)
    return resolveValue(target, seen)
  }
  if (typeof token.value !== 'string' || !token.value.trim() || /[;{}\r\n]/.test(token.value)) {
    throw new Error('Token value must be a non-empty CSS value')
  }
  return token.value
}

function visit(node) {
  if (!node || typeof node !== 'object') return
  if (Object.hasOwn(node, 'cssVar')) {
    if (!/^--qc-[a-z][a-z0-9-]*$/.test(node.cssVar) || variables.has(node.cssVar)) {
      throw new Error(`Invalid or duplicate CSS variable: ${node.cssVar}`)
    }
    variables.set(node.cssVar, resolveValue(node))
    return
  }
  for (const [key, child] of Object.entries(node)) {
    if (!key.startsWith('$')) visit(child)
  }
}
visit(document)
if (!variables.size) throw new Error('No CSS tokens found')
const declarations = [...variables].map(([name, value]) => `  ${name}: ${value};`)
const css = `/* Generated from design/admin.tokens.json. Run npm run tokens; do not edit directly. */
:root {
${declarations.join('\n')}
}
`
if (process.argv.includes('--check')) {
  if (await readFile(output, 'utf8') !== css) {
    throw new Error('Design token CSS is outdated. Run npm run tokens.')
  }
  console.log('Design token CSS is up to date.')
} else {
  await mkdir(new URL('../src/styles/', import.meta.url), { recursive: true })
  await writeFile(output, css)
  console.log(`Generated ${declarations.length} design tokens.`)
}
