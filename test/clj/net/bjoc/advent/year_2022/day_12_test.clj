(ns net.bjoc.advent.year-2022.day-12-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-12 :refer :all]))

(deftest path-length-by-example
  (is (= 31 (file->path-length "data/year_2022/day_12_example1.input"))))

(deftest best-path-length-by-example
  (is (= 29 (file->best-path-length "data/year_2022/day_12_example1.input"))))
