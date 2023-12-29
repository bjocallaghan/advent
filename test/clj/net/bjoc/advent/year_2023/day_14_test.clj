(ns net.bjoc.advent.year-2023.day-14-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.util.matrix :as mtx]
            [net.bjoc.advent.year-2023.day-14 :refer :all]))

(deftest file->load-by-example
  (is (= 136 (file->load "data/year_2023/day_14_example1.input"))))

(deftest tile-parity
  (is (= (file->load "data/year_2023/day_14_example1.input")
         (->> "data/year_2023/day_14_example1.input"
              mtx/from-file
              (tilt :north)
              calc-load-2))))

(deftest file->spin-load-by-example
  (is (= 64 (file->spin-load "data/year_2023/day_14_example1.input"))))
