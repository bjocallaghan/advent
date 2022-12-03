(ns net.bjoc.advent.year-2022.day-3-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-3 :refer :all]))

(deftest letter-score-by-examples
  (is (= 16 (letter-score \p)))
  (is (= 38 (letter-score \L)))
  (is (= 42 (letter-score \P))))

(deftest find-common-by-examples
  (is (= \p (find-common "vJrwpWtwJgWrhcsFMMfFFhFp")))
  (is (= \L (find-common "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL")))
  (is (= \P (find-common "PmmdzqPrVvPwwTWBwg"))))

(deftest priority-sum-by-example
  (is (= 157 (file->priority-sum "data/year_2022/day_3_example1.input"))))

(deftest badge-sum-by-example
  (is (= 70 (file->badge-sum "data/year_2022/day_3_example1.input"))))
