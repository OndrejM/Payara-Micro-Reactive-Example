Example how Payara Micro supports Reactive programming (CDI event bus, JCache).

`payara.reactive.rest.AsyncRESTResource.whatIsTheAnswer(AsyncResponse)` is the entrypoint for the demo. Requests computation via an outbound event and hooks callbacks to the appropriate CF future.

The requested computation is processed in external "computation" service, which again fires an outbound event with the result. If multiple computation services available, only one of them receives the event, due to synchronization using JCache locks. Enables to load-balance incoming requests for computation dynamically.