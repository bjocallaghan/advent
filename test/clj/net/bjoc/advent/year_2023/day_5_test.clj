(ns net.bjoc.advent.year-2023.day-5-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-5 :refer :all]))

(deftest line->fn-by-example
  (is (= 51 ((line->fn "50 98 2") 99)))
  (is (= nil ((line->fn "50 98 2") 4))))

(deftest spec->fn-by-example
  (let [my-fn (spec->fn ["seed-to-soil map:" "50 98 2" "52 50 48"])]
    (is (= 52 (my-fn 50)))
    (is (= 98 (my-fn 96)))
    (is (= 51 (my-fn 99)))
    (is (= 1 (my-fn 1)))))

(deftest file->xform-by-example
  (let [xform (file->xform "data/year_2023/day_5_example1.input")]
    (is (= 82 (xform 79)))
    (is (= 43 (xform 14)))
    (is (= 86 (xform 55)))
    (is (= 35 (xform 13)))))

(deftest file->best-location-by-example
  (is (= 35 (file->best-location "data/year_2023/day_5_example1.input"))))

(deftest file->best-location-2-by-example
  (is (= 46 (file->best-location-2 "data/year_2023/day_5_example1.input"))))
