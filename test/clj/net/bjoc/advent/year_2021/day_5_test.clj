(ns net.bjoc.advent.year-2021.day-5-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-5]))

(deftest num-dangerous-points-by-example
  (is (= 5 (file->num-dangerous-points "data/year_2021/day_5_example1.input"))))

(deftest segment-points-by-examples 
  (is (= [[5 5] [6 4] [7 3] [8 2]] (segment-points [[5 5] [8 2]])))
  (is (= [[5 5] [6 6] [7 7] [8 8]] (segment-points [[5 5] [8 8]]))))

(deftest num-dangerous-points-by-example
  (is (= 12 (file->num-dangerous-points* "data/year_2021/day_5_example1.input"))))
