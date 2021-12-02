(ns net.bjoc.advent.year-2021.day-1-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-1]))

(deftest num-depth-increases-by-example
  (is (= 7 (file->num-depths-increases "data/year_2021/day_1_example1.input"))))

(deftest num-window-increases-by-example
  (is (= 5 (file->num-window-increases "data/year_2021/day_1_example1.input"))))
