(ns net.bjoc.advent.year-2016.day-8-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2016.day-8 :refer :all]
            [net.bjoc.advent.util.matrix :refer [dump]]))

(deftest instructions-by-example
  (is (= ".#..#.#\n#.#....\n.#.....\n"
         (with-out-str
           (dump (file->screen [7 3] "data/year_2016/day_8_example1.input"))))))
