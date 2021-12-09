(ns net.bjoc.advent.year-2019.day-8-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2019.day-8])
  (:require [net.bjoc.advent.util.matrix :as mtx]))


(deftest best-layer-score-by-example
  (is (= 1 (file->best-layer-score "data/year_2019/day_8_example1.input"
                                   :width 3 :height 2))))

(deftest matrix-by-example
  (is (= (mtx/from-string "0 1\n1 0")
         (file->matrix "data/year_2019/day_8_example2.input" :width 2 :height 2))))
