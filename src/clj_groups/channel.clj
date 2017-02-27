(ns clj-groups.channel
  (:require [clj-groups.receiver :as receiver]))

(def channel (atom {}))

(defn- connect!
  "Joins to the specified cluster"
  [channel identifier callback]
  (.setReceiver channel callback)
  (.connect channel identifier))

(defn setup! 
  "Creates an instance of a channel and connects it via the given identifier. Uses default protocol settings" 
  [identifier callbacks]
  (reset! channel (org.jgroups.JChannel.))
  (connect! @channel identifier (receiver/receiver callbacks)))

(defn setup-with-settings!
  "Creates an instance of a channel and connects it via the given identifier. Uses the passed settings"
  [identifier settings]
  (reset! channel (org.jgroups.JChannel. settings))
  (connect! @channel identifier))

(defn send-message! 
  "Sends a message into the channel"
  [{^org.jgroups.Address target :target
    ^java.lang.Object payload :message}]   
  (.send @channel (org.jgroups.Message. target 
                                        payload)))
 
(defn teardown!
  "Closes the existing JChannel"
  []
  (.close @channel)
  (reset! channel {}))

 
