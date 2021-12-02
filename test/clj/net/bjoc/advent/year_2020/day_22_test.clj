(ns net.bjoc.advent.year-2020.day-22-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2020.day-22]))

(deftest combat-by-example
  (is (= 306 (file->winner-score "data/year_2020/day_22_example1.input"))))

(deftest recursive-combat-by-example
  (is (= 291 (file->winner-score* "data/year_2020/day_22_example1.input"))))
