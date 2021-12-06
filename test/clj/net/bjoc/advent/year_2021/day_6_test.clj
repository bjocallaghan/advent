(ns net.bjoc.advent.year-2021.day-6-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-6]))

(deftest population-at-day-by-examples
  (is (= 26 (file->population-at-day "data/year_2021/day_6_example1.input" 18)))
  (is (= 5934 (file->population-at-day "data/year_2021/day_6_example1.input" 80))))

(deftest sum-sanity
  (is (= 6 (sum 6)))
  (is (= 6 (sum 6 nil)))
  (is (= 6 (sum nil 6)))
  (is (= 6 (sum 2 4)))
  (is (= 6 (sum 4 2))))

(deftest population-at-day*-by-example
  (is (= 26984457539 (file->population-at-day* "data/year_2021/day_6_example1.input" 256))))
