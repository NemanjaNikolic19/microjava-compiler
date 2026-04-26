# MicroJava Compiler

## Overview

This project is a compiler for a MicroJava-like programming language, implemented in Java.
The current implementation corresponds to **Level A** of the project specification, covering the core compiler pipeline without advanced extensions.

It covers the main phases of a classical compiler pipeline:

- lexical analysis
- syntax analysis
- abstract syntax tree construction
- semantic analysis
- symbol-table management
- bytecode generation for the MicroJava virtual machine

The compiler was developed as an educational compiler-construction project and demonstrates how a high-level source program can be checked, analyzed, and translated into executable bytecode.

---

## Key Features

### Lexical Analysis

The lexer is specified using JFlex.

It recognizes:

- keywords such as `program`, `const`, `enum`, `print`, `read`, `return`, `if`, `else`, `new`, and `void`
- identifiers
- integer, character, and boolean constants
- arithmetic, relational, assignment, increment/decrement, and ternary operators
- array, field-access, and delimiter tokens
- single-line comments

---

### Syntax Analysis

The parser is specified using CUP.

The grammar supports:

- program declarations
- constants
- global and local variables
- arrays
- enum declarations
- method declarations
- formal and actual parameters
- assignments
- method calls
- `if-else` statements with dangling-else handling
- `read`, `print`, and `return` statements
- arithmetic and relational expressions
- ternary conditional expressions
- array allocation with `new`
- array indexing and `.length`

---

### Abstract Syntax Tree

The project uses generated AST classes and the visitor pattern.

Compiler phases operate over the syntax tree using visitors, which keeps parsing, semantic analysis, and code generation separated.

---

### Semantic Analysis

The semantic analyzer performs checks such as:

- existence of a valid `void main()` method
- declaration and use of symbols
- type checking for expressions and assignments
- validation of constants and enum values
- array indexing checks
- validation of `read` and `print` operands
- return-type checking for methods
- checking `++` and `--` operands
- compatibility checks for relational and ternary expressions

It also integrates with the symbol table used by the compiler infrastructure.

---

### Code Generation

The code generator emits bytecode for the MicroJava virtual machine.

Supported code generation includes:

- method entry and exit
- assignments
- constants
- local/global variable access
- array allocation and access
- array length access
- function and procedure calls
- `read` and `print`
- arithmetic operations
- unary minus
- relational expressions
- increment and decrement operations
- ternary conditional expressions

---

## Compiler Pipeline

```text
Source program (.mj)
        ↓
JFlex lexer
        ↓
CUP parser
        ↓
Abstract Syntax Tree
        ↓
Semantic Analyzer
        ↓
Code Generator
        ↓
MicroJava bytecode (.obj)
        ↓
MicroJava VM
```

---

## Project Structure

```text
MJCompiler/
├── spec/
│   ├── mjlexer.flex          # JFlex lexer specification
│   ├── mjparser.cup          # CUP grammar specification
│   └── mjparser_astbuild.cup # Grammar used for AST generation
│
├── src/rs/ac/bg/etf/pp1/
│   ├── SemanticAnalyzer.java # Semantic analysis visitor
│   ├── CodeGenerator.java    # MicroJava bytecode generation
│   ├── CounterVisitor.java   # Utility visitors for counting symbols
│   ├── MJParser.java         # Generated CUP parser
│   ├── Yylex.java            # Generated lexer
│   ├── sym.java              # Generated token definitions
│   ├── ast/                  # Generated AST classes
│   └── util/                 # Utility code
│
├── test/
│   ├── TEST1—BasicArithmetic+Unary.mj
│   ├── TEST2—Relational+Ternary.mj
│   ├── TEST3—Arrays+new+length.mj
│   ├── TEST4—++and--.mj
│   ├── TEST5—print_width+read.mj
│   └── TEST6—Dangling_else_safety.mj
│
├── lib/                      # Required compiler/runtime libraries
├── config/                   # Configuration files
└── logs/                     # Generated compiler logs
```

---

## Example Supported Language Features

```java
program Example

int x;
int[] arr;
const int five = 5;

{
    void main() {
        x = five + 3;
        arr = new int[10];

        arr[0] = x;
        print(arr.length);

        if (x > 0)
            print(x);
        else
            print(0);
    }
}
```

---

## Technologies

- Java
- JFlex
- CUP
- MicroJava runtime
- Symbol table library
- Log4j
- Visitor pattern

---

## What This Project Demonstrates

- compiler pipeline design
- lexical and syntax analysis
- grammar design
- AST-based compiler architecture
- semantic analysis and type checking
- symbol-table usage
- code generation for a stack-based virtual machine
- Java project organization

---

## Limitations

This is an educational compiler project, not a production compiler.

Current limitations include:

- supports the implemented subset of the MicroJava-like language
- no advanced optimizations
- no register allocation
- no native machine-code generation
- limited error recovery
- execution depends on the provided MicroJava runtime and project libraries

---

## Learning Goals

This project was built to gain hands-on experience with:

- how programming languages are tokenized and parsed
- how grammars are translated into AST structures
- how semantic rules are enforced
- how symbol tables are used during compilation
- how high-level constructs are translated into virtual-machine bytecode

---

## Author

Nemanja Nikolic  
School of Electrical Engineering (ETF), University of Belgrade
