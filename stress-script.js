import http from 'k6/http';
import { Counter, Trend } from 'k6/metrics';

export const options = {
    vus: 10,
    duration: '30s',
};

// Counters
const success = new Counter('success_requests');
const unauthorized = new Counter('unauthorized_requests');
const forbidden = new Counter('forbidden_requests');
const notFound = new Counter('not_found_requests');
const rateLimited = new Counter('rate_limited_requests');
const serverError = new Counter('server_error_requests');

// Response time trends
const successTime = new Trend('success_response_time');
const unauthorizedTime = new Trend('unauthorized_response_time');
const forbiddenTime = new Trend('forbidden_response_time');
const notFoundTime = new Trend('not_found_response_time');
const rateLimitedTime = new Trend('rate_limited_response_time');
const serverErrorTime = new Trend('server_error_response_time');

export default function () {

    const res = http.get('http://localhost:8080/product');

    switch (res.status) {

        case 200:
            success.add(1);
            successTime.add(res.timings.duration);
            break;

        case 401:
            unauthorized.add(1);
            unauthorizedTime.add(res.timings.duration);
            break;

        case 403:
            forbidden.add(1);
            forbiddenTime.add(res.timings.duration);
            break;

        case 404:
            notFound.add(1);
            notFoundTime.add(res.timings.duration);
            break;

        case 429:
            rateLimited.add(1);
            rateLimitedTime.add(res.timings.duration);
            break;

        default:
            if (res.status >= 500) {
                serverError.add(1);
                serverErrorTime.add(res.timings.duration);
            }
            break;
    }

    console.log(
        `Status=${res.status} | Time=${res.timings.duration.toFixed(2)} ms`
    );
}
