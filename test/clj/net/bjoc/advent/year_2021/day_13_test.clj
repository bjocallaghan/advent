(ns net.bjoc.advent.year-2021.day-13-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-13]))

(deftest post-first-fold-dot-count-by-example
  (is (= 17 (file->post-first-fold-dot-count "data/year_2021/day_13_example1.input"))))
