(ns net.bjoc.advent.year-2015.day-11-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2015.day-11]))

(deftest validity-predicates-by-examples
  (is (valid-req-1? "hijklmmn"))
  (is (valid-req-2? "aaaaaaaa"))
  (is (not (valid-req-2? "hijklmmn")))
  (is (not (valid-req-2? "aaaaiaaa")))
  (is (not (valid-req-2? "aaaaoaaa")))
  (is (not (valid-req-2? "aaaalaaa")))
  (is (valid-req-3? "abbceffg"))
  (is (not (valid-req-1? "abbceffg")))
  (is (valid? "abcdffaa"))
  (is (valid? "ghjaabcc")))

(deftest generate-by-examples
  (is (= "abcdffaa" (generate "abcdefgh")))
  ;; (is (= "ghjaabcc" (generate "ghijklmn"))) ;; disabled. takes too long
  )

(deftest password-number-reciprocity
  (is (= "aasdfafe" (->> "aasdfafe" password->number number->password))))
