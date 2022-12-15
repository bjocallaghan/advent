(ns net.bjoc.advent.year-2022.day-14-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-14 :refer :all]))

(deftest expand-by-examples
  (is (= #{[2 2]} (expand [2 2] [2 2])))
  (is (= #{[2 2] [2 3] [2 4]} (expand [2 2] [2 4])))
  (is (= #{[2 2] [2 3] [2 4]} (expand [2 4] [2 2])))
  (is (= #{[2 2] [3 2] [4 2]} (expand [2 2] [4 2])))
  (is (= #{[2 2] [3 2] [4 2]} (expand [4 2] [2 2]))))

(deftest grain-count-by-example
  (is (= 24 (file->grain-count* :abyss "data/year_2022/day_14_example1.input"))))

(deftest grain-count-with-floor-by-example
  (is (= 93 (file->grain-count* :floor "data/year_2022/day_14_example1.input"))))
