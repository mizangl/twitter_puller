package com.mz.twitterpuller.interactor;

import com.mz.twitterpuller.util.executor.PostExecutionThread;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.TestScheduler;
import java.util.concurrent.Executor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class) public class InteractorTest {

  @Rule public ExpectedException expectedException = ExpectedException.none();

  private InteractorTestClass interactor;
  private TestDisposableObserver<Object> testObserver;

  @Mock private Executor mockExecutor;
  @Mock private PostExecutionThread mockPostExecutionThread;

  @Before public void setUp() {
    interactor = new InteractorTestClass(mockExecutor, mockPostExecutionThread);
    testObserver = new TestDisposableObserver<>();
    given(mockPostExecutionThread.getScheduler()).willReturn(new TestScheduler());
  }

  @Test public void testBuildInteractorAndReturnCorrectResult() {
    interactor.execute(testObserver, Args.sEmpty);
    assertThat(testObserver.count).isZero();
  }

  @Test public void testSubscriptionWhenExecutingInteractor() {
    interactor.execute(testObserver, Args.sEmpty);
    interactor.dispose();

    assertThat(testObserver.isDisposed()).isTrue();
  }

  @Test public void testShouldFailWithNullObserver() {
    expectedException.expect(NullPointerException.class);
    interactor.execute(null, Args.sEmpty);
  }

  private static class InteractorTestClass extends Interactor<Object, Args> {

    public InteractorTestClass(Executor executor, PostExecutionThread postExecutionThread) {
      super(executor, postExecutionThread);
    }

    @Override Observable<Object> buildInteractor(Args args) {
      return Observable.empty();
    }
  }

  private static class Args {
    private static Args sEmpty = new Args();

    private Args() {
    }
  }

  private static class TestDisposableObserver<T> extends DisposableObserver<T> {
    private int count = 0;

    @Override public void onNext(T value) {
      count++;
    }

    @Override public void onError(Throwable e) {
      //
    }

    @Override public void onComplete() {
      //
    }
  }
}
