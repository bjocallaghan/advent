(ns net.bjoc.advent.year-2019.day-3-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2019.day-3]))

(deftest command->path-sanity
  (testing "something"
    (is (= [[3 2] [4 2] [5 2]] (command->path [2 2] [:right 3]) ))))

(deftest closest-intersection-by-examples
  (doseq [[filename expected-distance] [["data/year_2019/day_3_example1.input" 6]
                                        ["data/year_2019/day_3_example2.input" 159]
                                        ["data/year_2019/day_3_example3.input" 135]]]
    (testing "Day 3 - Wire intersection finder"
      (is (= expected-distance (file->closest-intersection filename))
          (format "Closest intersection of '%s' should have distance of %s"
                  filename expected-distance)))))

(deftest best-intersections-by-examples
  (doseq [[filename expected-distance] [["data/year_2019/day_3_example1.input" 30]
                                        ["data/year_2019/day_3_example2.input" 610]
                                        ["data/year_2019/day_3_example3.input" 410]]]
    (testing "Day 3 - Wire intersection finder part 2"
      (is (= expected-distance (file->best-intersection filename))
          (format "Best intersection of '%s' should have distance of %s"
                  filename expected-distance)))))
