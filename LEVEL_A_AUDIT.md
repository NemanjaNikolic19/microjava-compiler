# Level A audit (PP1/MicroJava)

## Environment and pull status
- Repository branch: `work`.
- `git pull` could not be executed because this repository has no configured tracking remote/branch.

## Commands executed
1. Build:
   - `javac -encoding UTF-8 -cp "MJCompiler/lib/*:MJCompiler/src:MJCompiler/test" -d MJCompiler/out $(find MJCompiler/src MJCompiler/test -name '*.java')`
2. Compile MJ Source (launch equivalent):
   - `java -cp "out:lib/*:config" rs.ac.bg.etf.pp1.MJParserTest test/test301.mj test/test301.obj` (working dir `MJCompiler`)
3. Run (launch equivalent with argument switched to `test\\test301.obj`):
   - `java -cp "out:lib/*" rs.etf.pp1.mj.runtime.Run -debug test/test301.obj` (working dir `MJCompiler`)
4. Sanity check with simpler Level A style input:
   - `java -cp "out:lib/*:config" rs.ac.bg.etf.pp1.MJParserTest test/TEST1—BasicArithmetic+Unary.mj test/basic.obj`
   - `java -cp "out:lib/*" rs.etf.pp1.mj.runtime.Run -debug test/basic.obj`

## Result summary
- Build succeeded.
- `test301.mj` **did not compile semantically** in current implementation (multiple semantic errors).
- Therefore `test/test301.obj` was not generated and Run reports file-not-found.
- Basic arithmetic/unary sample (`TEST1`) compiles and runs successfully.

## Strict Level A compliance assessment
Current project is **not yet fully compliant with Level A**.

### What is present
- Lexer recognizes required basics (identifiers, constants, keywords, operators, comments, whitespace skipping, lexical error message with line/column).
- Parser includes Level A core forms and has error recovery productions for:
  - global variable declaration (`ErrVarDeclSemi`, `ErrVarDeclComma`)
  - assignment statement (`ErrAssignment`)

### Blocking gaps for Level A
1. **Semantic handling of constants and enums is missing/incomplete**
   - No semantic visitor methods for `ConstDeclNum/Char/Bool` and enum declaration/item nodes are implemented.
   - As a consequence, symbols used from `const`/`enum` declarations are unresolved in semantic analysis.
2. **`bool` type support is missing in semantic symbol setup**
   - `bool` is used in test sources but is not inserted as a type in symbol table initialization in current code path.
3. **Code generation does not cover all Level A required expression/designator cases**
   - No code-gen visitors for ternary (`CondTernary`) and relational expression branching.
   - No explicit generation for `new` array allocation (`NewArray`) and `length` semantics.

## Recommended next improvements (Level A only)
1. Add semantic visitors for constant declarations and enum declarations/items, with symbol-table insertion and type checks.
2. Add `bool` type to semantic universe setup (or equivalent project convention), and enforce bool-constant typing consistently.
3. Implement missing Level A code generation for:
   - `new Type[Expr]`
   - ternary expression `CondFact ? Expr : Expr`
   - relational-expression evaluation used by ternary condition
   - `length` access behavior on arrays
4. Keep run configuration synced with compile output (`test\\test301.obj`) to avoid stale/incorrect runtime target.
