# Axios --- Compact Documentation & Examples

> A single-file practical reference for Axios, based on the official
> Axios documentation at https://axios.rest/.
>
> This is a **compact reference**, not a verbatim copy of the website.
> It consolidates the main APIs, configuration concepts, patterns, and
> representative examples.

------------------------------------------------------------------------

## 1. What is Axios?

Axios is a Promise-based HTTP client for browsers and Node.js. It
supports request/response interceptors, instances, TypeScript,
cancellation, authentication, uploads/downloads, progress events, custom
adapters, and multiple transports.

Typical flow:

``` text
Application
   ↓
Axios instance
   ↓
Request config
   ↓
Request interceptors
   ↓
Adapter (XHR / HTTP / Fetch)
   ↓
HTTP server
   ↓
Response
   ↓
Response transforms
   ↓
Response interceptors
   ↓
Application
```

------------------------------------------------------------------------

## 2. Installation

``` bash
npm install axios
```

``` bash
pnpm add axios
```

``` bash
yarn add axios
```

``` bash
bun add axios
```

Deno:

``` bash
deno install npm:axios
```

Browser CDN:

``` html
<script src="https://cdn.jsdelivr.net/npm/axios@<version>/dist/axios.min.js"></script>
```

Pin a version in production rather than relying on an unpinned CDN URL.

------------------------------------------------------------------------

## 3. Importing

### ESM

``` js
import axios from "axios";
```

Named exports:

``` js
import axios, {
  isCancel,
  AxiosError
} from "axios";
```

### CommonJS

``` js
const axios = require("axios");
```

### Browser/CommonJS bundles

``` js
const axios = require("axios/dist/browser/axios.cjs");
```

Node bundle:

``` js
const axios = require("axios/dist/node/axios.cjs");
```

------------------------------------------------------------------------

# 4. Basic Requests

## GET

``` js
const response = await axios.get("/users");

console.log(response.data);
```

## GET with path parameter

``` js
const response = await axios.get("/users/42");
```

## GET with query parameters

``` js
const response = await axios.get("/users", {
  params: {
    page: 2,
    limit: 20,
    active: true
  }
});
```

Axios serializes `params` into the query string.

## POST

``` js
const response = await axios.post("/users", {
  name: "Eduardo",
  email: "eduardo@example.com"
});
```

## PUT

``` js
await axios.put("/users/42", {
  name: "Eduardo"
});
```

## PATCH

``` js
await axios.patch("/users/42", {
  name: "Eduardo"
});
```

## DELETE

``` js
await axios.delete("/users/42");
```

## Generic request API

``` js
await axios.request({
  method: "POST",
  url: "/users",
  data: {
    name: "Eduardo"
  }
});
```

The default method is `GET` when `method` is omitted.

------------------------------------------------------------------------

# 5. Async/Await

Recommended general pattern:

``` js
async function getUsers() {
  try {
    const response = await axios.get("/users");

    return response.data;
  } catch (error) {
    console.error(error);
    throw error;
  }
}
```

With `finally`:

``` js
try {
  await axios.get("/users");
} catch (error) {
  console.error(error);
} finally {
  console.log("Request finished");
}
```

------------------------------------------------------------------------

# 6. Promise API

Axios requests return Promises.

``` js
axios
  .get("/users")
  .then(response => {
    console.log(response.data);
  })
  .catch(error => {
    console.error(error);
  })
  .finally(() => {
    console.log("Finished");
  });
```

------------------------------------------------------------------------

# 7. Axios Response

A response generally contains:

``` js
const response = await axios.get("/users");

response.data;
response.status;
response.statusText;
response.headers;
response.config;
response.request;
```

Most application code only needs:

``` js
const { data } = await axios.get("/users");
```

------------------------------------------------------------------------

# 8. Request Configuration

A request can be configured with an object:

``` js
await axios.get("/users", {
  baseURL: "https://api.example.com",
  timeout: 5000,
  headers: {
    Authorization: "Bearer token"
  },
  params: {
    page: 1
  }
});
```

Common options:

  Option                 Purpose
  ---------------------- -----------------------------------------
  `url`                  Request URL
  `method`               HTTP method
  `baseURL`              Prefix for relative URLs
  `headers`              HTTP headers
  `params`               URL query parameters
  `paramsSerializer`     Custom query serialization
  `data`                 Request body
  `timeout`              Request timeout
  `signal`               Abort/cancellation signal
  `auth`                 HTTP Basic authentication
  `responseType`         Expected response format
  `responseEncoding`     Node response encoding
  `withCredentials`      Send credentials in cross-site requests
  `validateStatus`       Decide which statuses resolve/reject
  `maxContentLength`     Node response-size limit
  `maxBodyLength`        Node request-body limit
  `transformRequest`     Transform request data
  `transformResponse`    Transform response data
  `adapter`              Select/customize transport
  `onUploadProgress`     Upload progress
  `onDownloadProgress`   Download progress

------------------------------------------------------------------------

# 9. Timeout

Always consider a timeout for production requests:

``` js
await axios.get("/users", {
  timeout: 5000
});
```

Without a timeout, a stalled request can remain pending indefinitely.

------------------------------------------------------------------------

# 10. Axios Instances

For real applications, create an instance:

``` js
import axios from "axios";

const api = axios.create({
  baseURL: "https://api.example.com",
  timeout: 5000
});
```

Then:

``` js
const response = await api.get("/users");
```

## Why use instances?

They centralize:

-   Base URL
-   Headers
-   Authentication
-   Timeout
-   Interceptors
-   Per-service configuration

Example:

``` js
const githubApi = axios.create({
  baseURL: "https://api.github.com"
});

const internalApi = axios.create({
  baseURL: "https://api.internal.example.com"
});
```

## Override instance defaults

``` js
const api = axios.create({
  timeout: 5000
});

await api.get("/slow", {
  timeout: 30000
});
```

Request-level config overrides instance defaults.

------------------------------------------------------------------------

# 11. Recommended Project Structure

For React/Vue/etc.:

``` text
src/
├── api/
│   ├── axios.js
│   ├── users.js
│   └── auth.js
├── services/
├── hooks/
├── components/
└── pages/
```

Example:

``` js
// api/axios.js
import axios from "axios";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 10000
});
```

``` js
// api/users.js
import { api } from "./axios";

export async function getUsers() {
  const { data } = await api.get("/users");
  return data;
}
```

------------------------------------------------------------------------

# 12. Headers

Request headers:

``` js
await axios.get("/users", {
  headers: {
    Authorization: "Bearer TOKEN",
    "X-Custom-Header": "value"
  }
});
```

Instance-wide headers:

``` js
const api = axios.create({
  baseURL: "https://api.example.com",
  headers: {
    "Content-Type": "application/json"
  }
});
```

Axios also provides header utilities on current v1.x configurations:

``` js
api.interceptors.request.use(config => {
  config.headers.set("X-App-Version", "1.0.0");
  return config;
});
```

------------------------------------------------------------------------

# 13. Authentication

## Bearer token

A common pattern is a request interceptor:

``` js
const api = axios.create({
  baseURL: "https://api.example.com"
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem("access_token");

  if (token) {
    config.headers.set("Authorization", `Bearer ${token}`);
  }

  return config;
});
```

## Basic authentication

``` js
await axios.get("/private", {
  auth: {
    username: "user",
    password: "password"
  }
});
```

`auth` is intended for HTTP Basic authentication and sets the
`Authorization` header.

------------------------------------------------------------------------

# 14. Interceptors

Interceptors behave similarly to middleware.

## Request interceptor

``` js
api.interceptors.request.use(
  config => {
    console.log("Request:", config.method, config.url);
    return config;
  },
  error => Promise.reject(error)
);
```

## Response interceptor

``` js
api.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    return Promise.reject(error);
  }
);
```

Common uses:

-   Add authentication
-   Logging
-   Refresh tokens
-   Normalize errors
-   Add correlation IDs
-   Centralize response handling

## Synchronous request interceptor

If an interceptor is intentionally synchronous:

``` js
axios.interceptors.request.use(
  config => {
    config.headers.set("X-Example", "value");
    return config;
  },
  null,
  { synchronous: true }
);
```

## Remove/clear interceptors

``` js
const id = axios.interceptors.request.use(config => config);

axios.interceptors.request.eject(id);
```

Current Axios also supports:

``` js
instance.interceptors.request.clear();
instance.interceptors.response.clear();
```

## Execution order

Multiple request interceptors execute in reverse registration order
(LIFO).

Response interceptors execute in registration order (FIFO).

------------------------------------------------------------------------

# 15. Error Handling

Basic:

``` js
try {
  await axios.get("/users");
} catch (error) {
  console.error(error);
}
```

Axios errors can contain:

``` js
error.message;
error.name;
error.code;
error.config;
error.request;
error.response;
```

## Server responded

``` js
if (error.response) {
  console.log(error.response.status);
  console.log(error.response.data);
}
```

## Request was made but no response

``` js
if (error.request && !error.response) {
  console.log("No response received");
}
```

## Request setup failed

``` js
if (!error.request && !error.response) {
  console.log("Request configuration/setup failed");
}
```

## Axios-specific error check

``` js
if (axios.isAxiosError(error)) {
  console.log(error.message);
}
```

Typed:

``` ts
if (axios.isAxiosError<ApiError>(error)) {
  console.log(error.response?.data);
}
```

------------------------------------------------------------------------

# 16. Important Error Codes

Common Axios error codes include:

  -----------------------------------------------------------------------
  Code                                Meaning
  ----------------------------------- -----------------------------------
  `ECONNABORTED`                      Request aborted, commonly
                                      timeout-related

  `ETIMEDOUT`                         Timeout

  `ERR_BAD_REQUEST`                   Invalid/bad request

  `ERR_BAD_RESPONSE`                  Unexpected/bad response

  `ERR_CANCELED`                      Request canceled

  `ERR_NETWORK`                       Network failure

  `ERR_INVALID_URL`                   Invalid URL

  `ERR_NOT_SUPPORT`                   Unsupported feature/environment

  `ERR_FORM_DATA_DEPTH_EXCEEDED`      Serialization exceeded configured
                                      depth
  -----------------------------------------------------------------------

------------------------------------------------------------------------

# 17. validateStatus

By default, Axios resolves successful HTTP responses and rejects
statuses outside its normal success range.

Customize this:

``` js
await axios.get("/users", {
  validateStatus(status) {
    return status < 500;
  }
});
```

Now 4xx responses can resolve instead of rejecting.

------------------------------------------------------------------------

# 18. Cancellation

Prefer `AbortController`.

``` js
const controller = new AbortController();

const request = axios.get("/users", {
  signal: controller.signal
});

controller.abort();
```

Handle cancellation:

``` js
try {
  await axios.get("/users", {
    signal: controller.signal
  });
} catch (error) {
  if (axios.isCancel(error)) {
    console.log("Request canceled");
  }
}
```

`CancelToken` is deprecated. Use `AbortController` for new code.

------------------------------------------------------------------------

# 19. Query Parameters

``` js
await axios.get("/products", {
  params: {
    search: "keyboard",
    page: 2,
    limit: 20
  }
});
```

Arrays/objects can be serialized according to Axios's parameter
serialization rules.

For custom behavior:

``` js
await axios.get("/products", {
  params: {
    tags: ["js", "axios"]
  },
  paramsSerializer: {
    indexes: false
  }
});
```

For more complex requirements, provide a custom serializer.

------------------------------------------------------------------------

# 20. Sending JSON

``` js
await axios.post("/users", {
  name: "Eduardo",
  age: 18
});
```

Axios handles JSON request data and response parsing in common cases.

------------------------------------------------------------------------

# 21. URL-Encoded Forms

Using `URLSearchParams`:

``` js
const params = new URLSearchParams();

params.append("username", "eduardo");
params.append("password", "secret");

await axios.post("/login", params);
```

------------------------------------------------------------------------

# 22. Multipart / FormData

``` js
const form = new FormData();

form.append("name", "Eduardo");
form.append("file", file);

await axios.post("/upload", form);
```

For a browser application, Axios can infer the appropriate multipart
content handling from `FormData`.

------------------------------------------------------------------------

# 23. File Upload

``` js
const form = new FormData();

form.append("file", file);

const response = await axios.post("/files", form);
```

Node applications can use compatible `FormData`/stream implementations
depending on the environment.

------------------------------------------------------------------------

# 24. File Download

Browser:

``` js
const response = await axios.get("/report.pdf", {
  responseType: "blob"
});
```

Node:

``` js
const response = await axios.get("https://example.com/file.zip", {
  responseType: "arraybuffer"
});
```

Other response types include:

``` text
json
text
blob
arraybuffer
document
stream
```

Availability depends on the environment.

------------------------------------------------------------------------

# 25. Response Types

Example:

``` js
const { data } = await axios.get("/image.png", {
  responseType: "arraybuffer"
});
```

Node stream:

``` js
const response = await axios.get("/large-file", {
  responseType: "stream"
});
```

------------------------------------------------------------------------

# 26. Upload/Download Progress

``` js
await axios.post("/upload", formData, {
  onUploadProgress(progressEvent) {
    console.log(progressEvent.loaded);
    console.log(progressEvent.total);
  }
});
```

Download:

``` js
await axios.get("/large-file", {
  onDownloadProgress(progressEvent) {
    console.log(progressEvent.loaded);
    console.log(progressEvent.total);
  }
});
```

The exact progress information depends on the adapter/environment.

------------------------------------------------------------------------

# 27. Cookies and Credentials

Browser requests can include credentials:

``` js
await axios.get("https://api.example.com/profile", {
  withCredentials: true
});
```

For cross-origin requests, server-side CORS configuration must also
permit the relevant credentials behavior.

------------------------------------------------------------------------

# 28. CSRF

Axios supports configuration for XSRF/CSRF cookies and headers.

Typical configuration:

``` js
const api = axios.create({
  xsrfCookieName: "XSRF-TOKEN",
  xsrfHeaderName: "X-XSRF-TOKEN"
});
```

The exact behavior depends on browser environment and server/CORS setup.

------------------------------------------------------------------------

# 29. Request/Response Transformation

Transform request data:

``` js
const api = axios.create({
  transformRequest: [
    (data, headers) => {
      // transform data
      return data;
    }
  ]
});
```

Transform response data:

``` js
const api = axios.create({
  transformResponse: [
    data => {
      // transform data
      return data;
    }
  ]
});
```

Transforms run as part of Axios's request/response pipeline.

------------------------------------------------------------------------

# 30. Defaults

Global defaults:

``` js
axios.defaults.baseURL = "https://api.example.com";
axios.defaults.timeout = 5000;
```

Instance defaults:

``` js
const api = axios.create({
  baseURL: "https://api.example.com"
});

api.defaults.timeout = 10000;
```

Prefer instances over modifying global defaults in larger applications.

------------------------------------------------------------------------

# 31. Config Precedence

A useful mental model:

``` text
Library defaults
      ↓
Instance defaults
      ↓
Request config
```

More specific configuration overrides less specific configuration.

Example:

``` js
const api = axios.create({
  timeout: 5000
});

await api.get("/slow", {
  timeout: 30000
});
```

The request uses 30 seconds.

------------------------------------------------------------------------

# 32. Security / Size Limits

In Node.js, content/body limits can be configured:

``` js
const api = axios.create({
  maxContentLength: 10 * 1024 * 1024,
  maxBodyLength: 10 * 1024 * 1024
});
```

These limits can be useful when communicating with servers that are not
fully trusted.

------------------------------------------------------------------------

# 33. Retry / Error Recovery

Axios itself exposes the primitives needed to implement retry behavior,
but retry policies are generally application-specific.

A simple manual pattern:

``` js
async function requestWithRetry(url, attempts = 3) {
  let lastError;

  for (let i = 0; i < attempts; i++) {
    try {
      return await axios.get(url);
    } catch (error) {
      lastError = error;
    }
  }

  throw lastError;
}
```

A production retry policy should consider:

-   Which errors are retryable
-   HTTP status
-   Idempotency
-   Backoff
-   Maximum attempts
-   Server rate limits
-   Cancellation
-   Duplicate side effects

Avoid blindly retrying POST operations.

------------------------------------------------------------------------

# 34. TypeScript

Axios includes TypeScript definitions.

``` ts
import axios from "axios";
```

## Typed response

``` ts
type User = {
  id: number;
  name: string;
  email: string;
};

const response = await axios.get<User>("/users/1");

console.log(response.data.name);
```

## Typed service function

``` ts
async function getUser(id: number): Promise<User> {
  const response = await axios.get<User>(`/users/${id}`);
  return response.data;
}
```

## Typed POST

``` ts
type CreateUser = {
  name: string;
  email: string;
};

type CreatedUser = CreateUser & {
  id: number;
};

async function createUser(
  data: CreateUser
): Promise<CreatedUser> {
  const response = await axios.post<CreatedUser>(
    "/users",
    data
  );

  return response.data;
}
```

## Typed instance

``` ts
import axios from "axios";
import type { AxiosInstance } from "axios";

const api: AxiosInstance = axios.create({
  baseURL: "https://api.example.com",
  timeout: 5000
});
```

## Typed interceptors

For Axios v1.x request interceptors, use `InternalAxiosRequestConfig`:

``` ts
import type { InternalAxiosRequestConfig } from "axios";

api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    config.headers.set(
      "Authorization",
      `Bearer ${getToken()}`
    );

    return config;
  }
);
```

## Typed errors

``` ts
type ApiError = {
  message: string;
  code: number;
};

try {
  await api.get("/protected");
} catch (error) {
  if (axios.isAxiosError<ApiError>(error)) {
    console.log(error.response?.data.message);
    console.log(error.response?.status);
  } else {
    throw error;
  }
}
```

------------------------------------------------------------------------

# 35. TypeScript Request Generics

Axios exposes request-related types such as:

``` ts
AxiosRequestConfig<D, P>
RawAxiosRequestConfig<D, P>
InternalAxiosRequestConfig<D, P>
AxiosDefaults<D, P>
CreateAxiosDefaults<D, P>

AxiosResponse<T, D, H, P>
AxiosPromise<T, D, P>
AxiosError<T, D, P>
CanceledError<T, D>
```

Conceptually:

``` text
T = response data
D = request body
P = query parameters
```

Example:

``` ts
type Body = {
  name: string;
};

type Params = {
  notify: boolean;
};

type Response = {
  id: number;
};
```

The request configuration can preserve the relationship between these
types.

------------------------------------------------------------------------

# 36. Adapter System

Axios delegates the actual HTTP operation to an adapter.

Common adapters:

``` text
xhr    → browser XHR
http   → Node.js HTTP
fetch  → Fetch API
```

Example:

``` js
const api = axios.create({
  adapter: "fetch"
});
```

Or:

``` js
const api = axios.create({
  adapter: "http"
});
```

You can also provide multiple adapter names:

``` js
const api = axios.create({
  adapter: ["xhr", "http", "fetch"]
});
```

Axios chooses the first supported adapter.

------------------------------------------------------------------------

# 37. Custom Adapter

Adapters can be used for:

-   Testing
-   Custom transports
-   Specialized environments
-   Mocking HTTP behavior

Conceptually:

``` js
const myAdapter = config => {
  return Promise.resolve({
    data: {},
    status: 200,
    statusText: "OK",
    headers: {},
    config,
    request: null
  });
};

const api = axios.create({
  adapter: myAdapter
});
```

A custom adapter must return an Axios-compatible response promise.

------------------------------------------------------------------------

# 38. Fetch Adapter

Fetch can be explicitly selected:

``` js
const api = axios.create({
  adapter: "fetch"
});
```

Fetch-based environments are useful when the runtime provides Fetch but
not XHR/Node's HTTP adapter.

------------------------------------------------------------------------

# 39. HTTP/2

Axios provides experimental HTTP/2 support through the Node.js HTTP
adapter in versions that support it.

``` js
const response = await axios.get(
  "https://example.com",
  {
    httpVersion: 2
  }
);
```

Additional options:

``` js
{
  httpVersion: 2,
  http2Options: {
    sessionTimeout: 5000
  }
}
```

HTTP/2 support is experimental. Redirect handling differs from the
normal HTTP/1.x behavior, so verify the current documentation before
relying on it.

------------------------------------------------------------------------

# 40. HTTP Methods

Axios request aliases include:

``` js
axios.request(config)

axios.get(url, config)
axios.post(url, data, config)
axios.put(url, data, config)
axios.patch(url, data, config)
axios.delete(url, config)
axios.head(url, config)
axios.options(url, config)
```

There are also form-oriented aliases in current Axios versions:

``` js
axios.postForm(url, data, config)
axios.putForm(url, data, config)
axios.patchForm(url, data, config)
```

------------------------------------------------------------------------

# 41. Common API Patterns

## CRUD service

``` js
import { api } from "./axios";

export const usersApi = {
  async list(params) {
    const { data } = await api.get("/users", { params });
    return data;
  },

  async getById(id) {
    const { data } = await api.get(`/users/${id}`);
    return data;
  },

  async create(user) {
    const { data } = await api.post("/users", user);
    return data;
  },

  async update(id, user) {
    const { data } = await api.put(`/users/${id}`, user);
    return data;
  },

  async remove(id) {
    await api.delete(`/users/${id}`);
  }
};
```

------------------------------------------------------------------------

# 42. Centralized Authentication

``` js
import axios from "axios";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 10000
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem("access_token");

  if (token) {
    config.headers.set("Authorization", `Bearer ${token}`);
  }

  return config;
});
```

------------------------------------------------------------------------

# 43. Centralized Error Handling

``` js
api.interceptors.response.use(
  response => response,
  error => {
    if (axios.isAxiosError(error)) {
      if (error.response?.status === 401) {
        // Handle authentication expiration
      }

      if (error.response?.status >= 500) {
        // Handle server-side failure
      }
    }

    return Promise.reject(error);
  }
);
```

Do not silently swallow errors unless that behavior is intentional.

------------------------------------------------------------------------

# 44. React Example

``` jsx
import { useEffect, useState } from "react";
import { api } from "./api/axios";

export default function Users() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const controller = new AbortController();

    async function loadUsers() {
      try {
        const { data } = await api.get("/users", {
          signal: controller.signal
        });

        setUsers(data);
      } catch (error) {
        if (!axios.isCancel(error)) {
          setError(error);
        }
      } finally {
        setLoading(false);
      }
    }

    loadUsers();

    return () => controller.abort();
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Failed to load users.</p>;

  return (
    <ul>
      {users.map(user => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  );
}
```

If using this exact example, import Axios as well:

``` js
import axios from "axios";
```

------------------------------------------------------------------------

# 45. Testing

Because Axios calls return Promises and instances are injectable, API
services can be tested by mocking the HTTP layer.

Keep API logic separate:

``` js
export async function getUser(id) {
  const { data } = await api.get(`/users/${id}`);
  return data;
}
```

Then test the service independently from UI components.

For application-level HTTP mocking, use the testing/mocking strategy
appropriate to your environment.

------------------------------------------------------------------------

# 46. Common Mistakes

### 1. No timeout

``` js
axios.get("/api");
```

Prefer:

``` js
axios.get("/api", {
  timeout: 10000
});
```

### 2. Repeating base URLs

Avoid:

``` js
axios.get("https://api.example.com/users");
axios.get("https://api.example.com/products");
axios.get("https://api.example.com/orders");
```

Prefer an instance:

``` js
const api = axios.create({
  baseURL: "https://api.example.com"
});
```

### 3. Catching without understanding the error

Don't assume every Axios error has `response`.

Use:

``` js
if (error.response) {
  // server responded
} else if (error.request) {
  // request made, no response
} else {
  // setup/configuration error
}
```

### 4. Using CancelToken for new code

Prefer:

``` js
AbortController
```

### 5. Blind retries

Do not automatically retry every request. Consider idempotency and side
effects.

### 6. Putting API calls everywhere

Centralize API communication in service modules or hooks.

------------------------------------------------------------------------

# 47. Quick Reference

## Most-used methods

``` js
axios.get(url, config)
axios.post(url, data, config)
axios.put(url, data, config)
axios.patch(url, data, config)
axios.delete(url, config)
axios.request(config)
```

## Most-used config

``` js
{
  baseURL,
  url,
  method,
  headers,
  params,
  data,
  timeout,
  signal,
  responseType,
  withCredentials,
  validateStatus
}
```

## Most-used response properties

``` js
response.data
response.status
response.statusText
response.headers
response.config
```

## Most-used error properties

``` js
error.message
error.code
error.response
error.request
error.config
```

## Most-used utilities

``` js
axios.isAxiosError(error)
axios.isCancel(error)
axios.create(config)
```

------------------------------------------------------------------------

# 48. Minimal Production Setup

``` js
// api.js
import axios from "axios";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 10000
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem("access_token");

  if (token) {
    config.headers.set("Authorization", `Bearer ${token}`);
  }

  return config;
});

api.interceptors.response.use(
  response => response,
  error => {
    if (axios.isAxiosError(error)) {
      console.error(
        "API error:",
        error.response?.status,
        error.response?.data
      );
    }

    return Promise.reject(error);
  }
);
```

Service:

``` js
// users.js
import { api } from "./api";

export async function getUsers(params) {
  const { data } = await api.get("/users", { params });
  return data;
}

export async function createUser(user) {
  const { data } = await api.post("/users", user);
  return data;
}
```

------------------------------------------------------------------------

# 49. Documentation Map

The official documentation is organized roughly as:

``` text
Getting Started
├── First steps
├── Features
└── Examples
    ├── JavaScript
    └── TypeScript

Advanced
├── Public API
├── Request method aliases
├── Creating an instance
├── Request config
├── Adapters
├── Response schema
├── Config defaults
├── Interceptors
├── Error handling
├── Cancellation
├── Authentication
├── Retry & error recovery
├── Testing
├── URL-encoded format
├── Multipart/form-data
├── File posting
├── HTML form processing
├── Progress capturing
├── Rate limiting
├── Headers
├── Fetch adapter
├── HTTP/2
├── Promises
└── TypeScript

Miscellaneous
├── SemVer
└── Security
```

------------------------------------------------------------------------

# 50. Practical Decision Guide

### Simple request

``` js
axios.get("/users");
```

### Multiple requests to one API

``` js
const api = axios.create({
  baseURL: "https://api.example.com"
});
```

### Authentication

Use an instance + request interceptor.

### Cancel a request

Use `AbortController`.

### Upload a file

Use `FormData`.

### Download binary data

Set an appropriate `responseType`.

### Global API error handling

Use a response interceptor.

### Strong TypeScript typing

Type response data with generics:

``` ts
axios.get<User>("/users/1");
```

### Different backends

Create separate Axios instances.

### Custom transport/testing

Use an adapter.

------------------------------------------------------------------------

# 51. Official References

-   Axios documentation: https://axios.rest/
-   Getting started:
    https://axios.rest/pages/getting-started/first-steps
-   API reference: https://axios.rest/pages/advanced/api-reference
-   Request config: https://axios.rest/pages/advanced/request-config
-   Instances: https://axios.rest/pages/advanced/create-an-instance
-   Interceptors: https://axios.rest/pages/advanced/interceptors
-   Error handling: https://axios.rest/pages/advanced/error-handling
-   Authentication: https://axios.rest/pages/advanced/authentication
-   TypeScript examples:
    https://axios.rest/pages/getting-started/examples/typescript
-   Adapters: https://axios.rest/pages/advanced/adapters
-   HTTP/2: https://axios.rest/pages/advanced/http2

------------------------------------------------------------------------

## Version note

Axios evolves over time. This reference targets the current v1.x
documentation concepts and intentionally avoids copying the complete
website. Check the official documentation when relying on a
version-specific option or experimental feature.
