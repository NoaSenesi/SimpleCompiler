# Simple Compiler

This is a compiler for a simple language. It won't have complex features, but the strict minimum. It generates C code, then compiles it with GCC.
You can find examples of the language in the `example.sp` and `fibonacci.sp` files.

The compiler uses a LR(1) parser, generated from the grammar in `grammar.g2t`. The tool used to generate the parse table is `Grammar2Table`, which you can find [here](https://github.com/NoaSenesi/Grammar2Table).\
With this tool, the command used was `g2t grammar.g2t -p5 -c -n`.

## Build

Use the command `build` for Windows, or `./build` for Linux.

## Run

Use the command `simple <file> [parameters]` for Windows, or `./simple <file> [parameters]` for Linux.

### Parameters
- `-c`: Doesn't compile the file to machine code, but to C language. However, build files are created.
- `-t`: Shows the AST.
