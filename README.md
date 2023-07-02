# Simple Compiler

This is a compiler for a simple language. It won't have complex features, but the strict minimum. I plan to add Assembly code generation, but for now it will be itself.
You can find an example of the language in the `example.sp` file.

The compiler uses a LR(1) parser, generated from the grammar in `grammar.g2t`. The tool used to generate the parse table is `Grammar2Table`, which you can find [here](https://github.com/NoaSenesi/Grammar2Table).
The command used to generate the parse table is `g2t grammar.g2t -p5 -c -n`.

## Build

Use the command `build` for Windows, or `./build` for Linux.

## Run

Use the command `simple <file> [-t]` for Windows, or `./simple <file> [-t]` for Linux. The `-t` flag is optional, and will print the AST.

##