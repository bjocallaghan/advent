(ns net.bjoc.advent.year-2023.day-12-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-12 :refer :all]))

(deftest line->num-expansions-by-example
  (is (= 1 (line->num-expansions "???.### 1,1,3")))
  (is (= 4 (line->num-expansions ".??..??...?##. 1,1,3")))
  (is (= 1 (line->num-expansions "?#?#?#?#?#?#?#? 1,3,1,6")))
  (is (= 1 (line->num-expansions "????.#...#... 4,1,1")))
  (is (= 4 (line->num-expansions "????.######..#####. 1,6,5")))
  (is (= 10 (line->num-expansions "?###???????? 3,2,1"))))

(deftest file->num-expansions-sum-by-example
  (is (= 21 (file->num-expansions-sum "data/year_2023/day_12_example1.input"))))

(deftest parse-line-2-by-example
  (is (= {:conditions ".#?.#?.#?.#?.#" :spec [1 1 1 1 1]} (parse-line-2 ".# 1")))
  (is (= {:conditions "???.###????.###????.###????.###????.###" :spec [1,1,3,1,1,3,1,1,3,1,1,3,1,1,3]} (parse-line-2 "???.### 1,1,3"))))

(deftest file->num-expansions-sum-2-by-example
  (is (= 525152 (file->num-expansions-sum-2 "data/year_2023/day_12_example1.input"))))
