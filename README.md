# SaxGen
SAX handler generator for Java, inspired by Javacc.

Writing SAX handlers by hand can be tedious, especially when XML file structure is complex.
SaxGen will do this dirty job for you. Just describe file structure using Javacc-like grammar,
and SaxGen will generate an efficient state-machine-based SAX handler.

The repository contains SaxGen as well as a DemoProject illustrating its usage.
Both can be built with Maven: `mvn clean package`
