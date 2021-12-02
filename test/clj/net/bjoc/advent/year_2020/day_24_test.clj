(ns net.bjoc.advent.year-2020.day-24-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2020.day-24]))

(deftest num-flips-by-example
  (is (= 10 (file->num-flips "data/year_2020/day_24_example1.input"))))

(deftest evolution-flip-counts-by-examples
  (doseq [[steps expected-flips] {1 15, 2 12, 3 25, 4 14, 5 23,
                                       6 28, 7 41, 8 37, 9 49, 10 37}]
    (file->num-evolved-flips "data/year_2020/day_24_example1.input" steps)))
