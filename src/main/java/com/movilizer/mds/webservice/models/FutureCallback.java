/*
 * Copyright 2015 Movilizer GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.movilizer.mds.webservice.models;

/**
 * This is <tt>Future</tt> mimics the behavior and API of Scala Futures for easier integration Scala since Java Futures
 * are blocking... This interface is intended to be use to create anonymous classes when async methods are to be used.
 * See the following example:
 * <p/>
 * Async method call:
 * <pre>
 * {@code
 * final Persistence service = new Persistence();
 * myAsyncCalculation("http://path.to/rest/integer/resource", new FutureCallback<Integer> {
 *     void onSuccess(Integer myExternalNumber){
 *         service.save(2 + myExternalNumber);
 *     }
 *     void onComplete(Integer myExternalNumber, Exception futureException) {}
 *     void onFailure(Exception futureException) {}
 * });
 * </pre>
 * <p/>
 * Async method implementation:
 * <pre>
 * {@code
 * void myAsyncCalculation(String resourceURI, FutureCallback<Integer> myFuture) {
 *      //Omitted async call to get the resource
 *      try {
 *          Integer externalNumber = get(resourceURI);
 *          myFuture.onSuccess(externalNumber);  //FIRST CALL SUCCESS
 *          myFuture.onComplete(externalNumber, null);  //onComplete IS CALLED LAST
 *      } catch (Exception e) {
 *          myFuture.onFailure(e);
 *          myFuture.onComplete(null, e);  //onComplete IS CALLED LAST
 *      }
 * }
 * </pre>
 * <p/>
 * <b>IMPORTANT:</b> Notice the following behaviour. On future completion, either <tt>onSuccess</tt> or
 * <tt>onFailure</tt> will be called once and then, independently of the result of the future, the onComplete will be
 * called. <b>THIS BEHAVIOUR MUST BE FOLLOWED WHEN IMPLEMENTING FUNCTIONS THAT RESOLVE THE GIVEN FUTURES</b>.
 * <p/>
 * For extra information on Futures and Scala Futures see:
 * <a href="http://docs.scala-lang.org/overviews/core/futures.html">Futures and Promises</a>
 *
 * @param <T> The future instance of the variable we want to work with.
 * @author Jesús de Mula Cano
 * @since 12.11.1.0
 */
public interface FutureCallback<T> {
    /**
     * This method will be called when the async call has been completed successfully.
     *
     * @param futureVar the expected result of the async call to be used inside the method.
     * @since 12.11.1.0
     */
    void onSuccess(T futureVar);

    /**
     * This method will be called when the async call has been completed either successfully or with an exception.
     * <b>IMPORTANT:</b> This method will ALWAYS be called LAST after <tt>onSuccess</tt> or <tt>onFailure</tt>.
     *
     * @param futureVar       the expected result of the async call to be used inside the method or <tt>null</tt>.
     * @param futureException the error that occurred during the async call or <tt>null</tt>.
     * @since 12.11.1.0
     */
    void onComplete(T futureVar, Exception futureException);

    /**
     * This method will be called when the async call has been completed unsuccessfully.
     *
     * @param futureException the error that occurred during the async call.
     * @since 12.11.1.0
     */
    void onFailure(Exception futureException);
}
