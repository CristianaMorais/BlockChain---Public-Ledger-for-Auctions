// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: PingService.proto

package ssd;

public interface PingRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:PingRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional string nodeID = 1;</code>
   */
  java.lang.String getNodeID();
  /**
   * <code>optional string nodeID = 1;</code>
   */
  com.google.protobuf.ByteString
      getNodeIDBytes();

  /**
   * <code>optional .NodeInfo sender = 2;</code>
   */
  boolean hasSender();
  /**
   * <code>optional .NodeInfo sender = 2;</code>
   */
  ssd.NodeInfo getSender();
  /**
   * <code>optional .NodeInfo sender = 2;</code>
   */
  ssd.NodeInfoOrBuilder getSenderOrBuilder();
}
