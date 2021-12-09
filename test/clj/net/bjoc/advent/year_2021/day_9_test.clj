(ns net.bjoc.advent.year-2021.day-9-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-9]))

(deftest risk-sum-by-example
  (is (= 15 (file->risk-sum "data/year_2021/day_9_example1.input"))))

(deftest basin-size-by-examples
  (let [matrix (file->matrix "data/year_2021/day_9_example1.input")]
    (is (= 3 (basin-size matrix [[1 0] 2])))
    (is (= 9 (basin-size matrix [[9 0] 0])))
    (is (= 14 (basin-size matrix [[2 2] 5])))
    (is (= 9 (basin-size matrix [[6 4] 5])))))

(deftest largest-basins-product-by-example
  (is (= 1134 (file->largest-basins-product "data/year_2021/day_9_example1.input"))))
