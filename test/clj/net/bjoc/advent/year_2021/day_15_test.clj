(ns net.bjoc.advent.year-2021.day-15-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-15]))

(deftest lowest-danger-by-example
  (is (= 40 (file->lowest-danger "data/year_2021/day_15_example1.input" danger-matrix)))
  (is (= 5 (file->lowest-danger "data/year_2021/day_15_example2.input" danger-matrix)))
  (is (= 40 (file->lowest-danger "data/year_2021/day_15_example1.input")))
  (is (= 5 (file->lowest-danger "data/year_2021/day_15_example2.input"))))

(deftest lowest-danger*-by-example
  (is (= 315 (file->lowest-danger* "data/year_2021/day_15_example1.input"))))
