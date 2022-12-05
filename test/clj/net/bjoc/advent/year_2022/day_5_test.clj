(ns net.bjoc.advent.year-2022.day-5-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-5 :refer :all]))

(deftest line->stacks-by-example
  (is (= {1 '(\N \Z) 2 '(\D \C \M) 3 '(\P)}
         (lines->stacks ["    [D]    "
                         "[N] [C]    "
                         "[Z] [M] [P]"
                         "1   2   3 "]))))

(deftest top-crates-by-example
  (is (= "CMZ" (file->top-crates "data/year_2022/day_5_example1.input"))))

(deftest top-crates-by-example
  (is (= "MCD" (file->top-crates-2 "data/year_2022/day_5_example1.input"))))
