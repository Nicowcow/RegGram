# RegGram

## DNA Toehold exchange simulator and analyzer

**RegGram** is a java-based simulator and analyzer for DNA toehold exchange reactions, which I developed during my semester project *Parallel Computing with DNA*. 

## Important classes


* `Strand` in the `core` package represents a DNA strand in memory. A DNA strand is abstracted from nucletodies (A,C,T,G) to any symbol (any `char`, really). Each strand is represented as a tree of operations and terminals: terminals are any symbol, and operations are either *WK-Complementarity* or repetition of a symbol.

* `Matcher` in the `processing` package determins if two DNA strands match (possibly partially). A Matcher is usually called during an `Experiment`, which takes a `Soup` (basic configuration) and produces outputs. Two `Soup`s are implemented in the `processing.soups` package: 
 * `UglySoup`, where strand matches and evolution of the system are assumed to occur in continuous time, and anlysis is done with continous-time Markov chains.
 * `NiceSoup`, which is measures the maximal state space size reachable with a given configuration.


## More information

You can contact me to get a copy of the Semester project report *Parallel Computing with DNA* or the subsequent paper *Toehold DNA Languages are Regular*.
