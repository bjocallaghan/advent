(ns net.bjoc.advent.year-2022.day-11-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-11 :refer :all]))

(deftest monkey-business-by-example
  (is (= 10605 (file->monkey-business-1 "data/year_2022/day_11_example1.input"))))

(deftest monkey-business-by-example
  (is (= 2713310158 (file->monkey-business-2 "data/year_2022/day_11_example1.input"))))
