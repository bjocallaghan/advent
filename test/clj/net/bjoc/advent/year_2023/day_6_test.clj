(ns net.bjoc.advent.year-2023.day-6-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-6 :refer :all]))

(deftest file->num-winning-scenarios-by-example
  (is (= [4 8 9] (file->num-winning-scenarios "data/year_2023/day_6_example1.input"))))

(deftest file->num-winning-scenarios-2-by-example
  (is (= 71503 (file->num-winning-scenarios-2 "data/year_2023/day_6_example1.input"))))
