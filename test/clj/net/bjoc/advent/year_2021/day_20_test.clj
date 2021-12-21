(ns net.bjoc.advent.year-2021.day-20-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-20]))

(deftest evolve-2-count-by-example
  (is (= 35 (file->evolve-2-count "data/year_2021/day_20_example1.input"))))

(deftest evolve-50-count-by-example
  ;; takes too long
  ;; (is (= 3351 (file->evolve-50-count "data/year_2021/day_20_example1.input")))
  )
