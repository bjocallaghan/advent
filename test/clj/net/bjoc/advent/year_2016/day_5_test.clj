(ns net.bjoc.advent.year-2016.day-5-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2016.day-5 :refer :all]))

(deftest part-1-tools
  (testing "finding the next offset"
    (is (= 3231929 (:offset (find-next-offset "abc" 3231920))))
    (is (= 5017308 (:offset (find-next-offset "abc" 5017300)))))
  (testing "recognizing interesting hashes"
    (is (interesting? "000008f82"))
    (is (not (interesting? "100008f82")))))
    
(deftest provided-samples
  (comment
    ;; time-intensive; don't run when running all tests
    (is (= "18f47a30" (seed->password "abc")))
    (is (= "05ace8e3" (seed->password-2 "abc")))
    ))
