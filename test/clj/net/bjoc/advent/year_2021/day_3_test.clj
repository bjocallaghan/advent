(ns net.bjoc.advent.year-2021.day-3-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-3]))

(def diagnostics (file->diagnostics "data/year_2021/day_3_example1.input"))

(deftest gamma-rate-by-example
  (is (= 22 (gamma-rate diagnostics))))

(deftest epsilon-rate-by-example
  (is (= 9 (epsilon-rate diagnostics))))

(deftest power-consumption-by-example
  (is (= 198 (file->power-consumption "data/year_2021/day_3_example1.input"))))

(deftest oxygen-generator-rating-by-example
  (is (= 23 (oxygen-generator-rating diagnostics))))

(deftest co2-scrubber-rating-by-example
  (is (= 10 (co2-scrubber-rating diagnostics))))

(deftest file->life-support-rating-by-example
  (is (= 230 (file->life-support-rating "data/year_2021/day_3_example1.input"))))
