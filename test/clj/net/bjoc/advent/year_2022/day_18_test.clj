(ns net.bjoc.advent.year-2022.day-18-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-18 :refer :all]))

(def mega-cube
  (set (for [x (range 0 5)
             y (range 0 5)
             z (range 0 5)]
         [x y z])))

(def mega-cube-with-hole (-> mega-cube (disj [1 1 1] [1 1 2])))

(def cubic-void
  (set (for [x (range 1 4)
             y (range 1 4)
             z (range 1 4)]
         [x y z])))

(def hollow-mega-cube (clojure.set/difference mega-cube cubic-void))

(deftest num-exposed-faces-by-example
  (is (= 64 (file->num-exposed-faces "data/year_2022/day_18_example1.input")))
  (is (= 150 (num-exposed-faces mega-cube)))
  (is (= 160 (num-exposed-faces mega-cube-with-hole)))
  (is (= 204 (num-exposed-faces hollow-mega-cube))))

(deftest num-exterior-faces-by-example
  (is (= 58 (file->num-exterior-faces "data/year_2022/day_18_example1.input")))
  (is (= 150 (num-exterior-faces mega-cube)))
  (is (= 150 (num-exterior-faces (-> mega-cube (disj [1 1 1] [1 1 2])))))
  (is (= 150 (num-exterior-faces (-> mega-cube (disj [0 0 0])))))
  (is (= 150 (num-exterior-faces (-> mega-cube (disj [1 1 1] [2 2 2])))))
  (is (= 150 (num-exterior-faces (-> mega-cube (disj [1 1 1] [2 2 2])))))
  (is (= 152 (num-exterior-faces (-> mega-cube (disj [0 3 0])))))
  (is (= 154 (num-exterior-faces (-> mega-cube (disj [0 3 0] [0 3 1])))))
  (is (= 12 (num-exterior-faces #{[0 0 0] [1 1 1]})))
  (is (= 150 (num-exterior-faces hollow-mega-cube)))
  (is (= 108 (num-exterior-faces (set (concat (for [x (range 0 3)
                                                    y (range 0 3)
                                                    z (range 0 3)]
                                                [x y z])
                                              (for [x (range 3 6)
                                                    y (range 3 6)
                                                    z (range 3 6)]
                                                [x y z]))))))
  ;; interesting observation: does not work if main body isn't a single unit:
  ;; i.e. 2+ legitimate outer shells
  (comment
    (is (= 108 (num-exterior-faces (set (concat (for [x (range 0 3)
                                                      y (range 0 3)
                                                      z (range 0 3)]
                                                  [x y z])
                                                (for [x (range 4 7)
                                                      y (range 4 7)
                                                      z (range 4 7)]
                                                  [x y z]))))))
  ))
