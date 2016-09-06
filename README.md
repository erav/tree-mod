# tree-mod 

[snapshot jar and sources](https://oss.sonatype.org/content/repositories/snapshots/com/github/erav/tree-mod/0.0.1-SNAPSHOT/)

Useful actions on trees comprised of java.util.Map and java.util.List, and a JSON implementation thereof.
Two basic pass modes are available:
 - depth-first traversal of all nodes in the tree
 - navigation of specific branches\path in the tree

For each pass modes relevant actions are available. For example:
 - removal of nodes
 - retainment of specified branches
 - branch finder
 - replicating parts of a tree

Currently being used in prouction in an >800M users big data server.
