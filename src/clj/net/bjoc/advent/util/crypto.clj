(ns net.bjoc.advent.util.crypto
  (:import [org.apache.commons.codec.digest DigestUtils]))

(defn md5
  "Return the MD5 hash of a string."
  [s]
  (DigestUtils/md5Hex s))
