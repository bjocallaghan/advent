(ns net.bjoc.advent.year-2016.day-1-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2016.day-1]))

(deftest distance-by-examples
  (is (= 5 (filename->distance "data/year_2016/day_1_example1.input")))
  (is (= 2 (filename->distance "data/year_2016/day_1_example2.input")))
  (is (= 12 (filename->distance "data/year_2016/day_1_example3.input"))))

(deftest walk-until-by-example
  (is (= 4 (filename->walk-until "data/year_2016/day_1_example4.input"))))
