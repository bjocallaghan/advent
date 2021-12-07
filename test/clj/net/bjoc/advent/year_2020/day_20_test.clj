(ns net.bjoc.advent.year-2020.day-20-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2020.day-20])
  (:require [net.bjoc.advent.util.matrix :as mtx]))

(deftest product-of-corners-by-example
  (is (= 20899048083289
         (file->product-of-corners "data/year_2020/day_20_example1.input"))))

(deftest rough-water-score-by-example
  (let [m (mtx/from-file "data/year_2020/day_20_example2.input")]
    (is (= 273 (rough-water-score m)))))

(deftest assembly-by-example
  (let [expected (mtx/from-file "data/year_2020/day_20_example2.input")
        actual (-> (file->tiles "data/year_2020/day_20_example1.input")
                   assemble)]
    (is (mtx/transform? expected actual))))
