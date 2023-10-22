(ns net.bjoc.advent.year-2016.day-2-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2016.day-2 :refer :all]))

(deftest file->code-by-example
  (is (= "1985" (file->code "data/year_2016/day_2_example1.input"))))

(deftest file->code-by-example
  (is (= "5DB3" (file->code-2 "data/year_2016/day_2_example1.input"))))
