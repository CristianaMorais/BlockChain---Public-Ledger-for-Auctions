package ssd;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.1)",
    comments = "Source: FindValueService.proto")
public class FindValueServiceGrpc {

  private FindValueServiceGrpc() {}

  public static final String SERVICE_NAME = "FindValueService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<ssd.FindValue,
      ssd.NodeReply> METHOD_FIND_V =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "FindValueService", "FindV"),
          io.grpc.protobuf.ProtoUtils.marshaller(ssd.FindValue.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(ssd.NodeReply.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FindValueServiceStub newStub(io.grpc.Channel channel) {
    return new FindValueServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FindValueServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new FindValueServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static FindValueServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new FindValueServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class FindValueServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void findV(ssd.FindValue request,
        io.grpc.stub.StreamObserver<ssd.NodeReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_FIND_V, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_FIND_V,
            asyncUnaryCall(
              new MethodHandlers<
                ssd.FindValue,
                ssd.NodeReply>(
                  this, METHODID_FIND_V)))
          .build();
    }
  }

  /**
   */
  public static final class FindValueServiceStub extends io.grpc.stub.AbstractStub<FindValueServiceStub> {
    private FindValueServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FindValueServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FindValueServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FindValueServiceStub(channel, callOptions);
    }

    /**
     */
    public void findV(ssd.FindValue request,
        io.grpc.stub.StreamObserver<ssd.NodeReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_FIND_V, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class FindValueServiceBlockingStub extends io.grpc.stub.AbstractStub<FindValueServiceBlockingStub> {
    private FindValueServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FindValueServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FindValueServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FindValueServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ssd.NodeReply findV(ssd.FindValue request) {
      return blockingUnaryCall(
          getChannel(), METHOD_FIND_V, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class FindValueServiceFutureStub extends io.grpc.stub.AbstractStub<FindValueServiceFutureStub> {
    private FindValueServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FindValueServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FindValueServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FindValueServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ssd.NodeReply> findV(
        ssd.FindValue request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_FIND_V, getCallOptions()), request);
    }
  }

  private static final int METHODID_FIND_V = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final FindValueServiceImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(FindValueServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_FIND_V:
          serviceImpl.findV((ssd.FindValue) request,
              (io.grpc.stub.StreamObserver<ssd.NodeReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_FIND_V);
  }

}
