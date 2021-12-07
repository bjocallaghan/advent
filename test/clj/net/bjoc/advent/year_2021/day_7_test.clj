(ns net.bjoc.advent.year-2021.day-7-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-7]))

(deftest fleet-expenditure-by-examples
  (let [positions (file->positions "data/year_2021/day_7_example1.input")]
    (is (= 37 (fleet-expenditure positions 2)))
    (is (= 41 (fleet-expenditure positions 1)))
    (is (= 39 (fleet-expenditure positions 3)))
    (is (= 71 (fleet-expenditure positions 10)))))

(deftest best-expenditure-by-example
  (is (= 37 (file->best-expenditure "data/year_2021/day_7_example1.input"))))

(deftest fleet-expenditure*-by-examples
  (let [positions (file->positions "data/year_2021/day_7_example1.input")]
    (is (= 168 (fleet-expenditure* positions 5)))
    (is (= 206 (fleet-expenditure* positions 2)))))

(deftest best-expenditure*-by-example
  (is (= 168 (file->best-expenditure* "data/year_2021/day_7_example1.input"))))
