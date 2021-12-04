# Quickstart

To run, supply the year and day at the command prompt:

`clojure -X:run :year 2021 :day 2`

To test:

`clojure -X:test`

# Introduction

[Advent of Code](adventofcode.com) is a yearly event--25 magical programming
puzzles!

I've been doing Advent of Code somewhat halfheartedly for the past several
years. In 2021 I decided to be a bit more disciplined with my code (included a
"runner", a common library for "utilities", tests).

I also scooped up some of my code from previous years and jammed it into the
repository. Some of it works "as-is", some might need improvement. I have
aspirations to some day work my way through the "backlog" of previous years and
fill in the gaps.

# Selective overview: using the utility libraries

## util/matrix.clj

Many Advent of Code puzzles use tables, two-dimensional data, or some other
contrived data structure which can be thought of as a matrix. I've created some
utilities for working with matrices, especially reading, writing, and
visualizing.

```clojure
(def raw-matrix-string
  " 9 15  6
   13  4 18
    4 13  7")

(require '[net.bjoc.advent.util.matrix :as mtx])

(def my-matrix
  (mtx/from-string raw-matrix-string)) ;; cool, we can automagically "read" a matrix

(mtx/dump my-matrix)                   ;; dumping it to stdout is easy

(-> my-matrix
    (mtx/rotate 2)
    mtx/dump)                          ;; string together operations before dumping
```
