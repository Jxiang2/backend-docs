## Spring

### *Bean Scope*

1. default: singleton, only instantiate the class once when accessing a bean from ApplicationContext
2. prototype: create multiple instances when accessing a bean from ApplicationContext multiple times

### *Bean Lifecycle*

1. initialize IOC container
    1. assign memories for objects accroding to applicationContext.xml
    2. excute the constructor methods of the objects
    3. excute DI setters
    4. excute bean init method through init() with applicationContext.xml; Or afterPropertiesSet() from InitializingBean
       interface
2. use beans in IOC container
    1. bussiness logics ...
3. destroy beans
    1. excute the destroy() method from DisposableBean interface
    2. excute an anotherDestroy() method with applicationContext.xml, if applicable
    3. bean destroyed, with excuting ctx.close() or ctx.registerShutdownHook() in main method

### *AOP practice*

1. enable AOP in SpringConfig file; Create @Aspect bean to hold aspects, the class is named *Advice*

```java
// SpringConfig.java
@Configuration
@ComponentScan("com.project")
@EnableAspectJAutoProxy
public class SpringConfig {
        ...
}

@Component
@Aspect
public class MyAdvice {
        ...
}
```

2. create @Pointcut("execution()") decorated methods to match old methods need to be enhanced.

```java
@Pointcut("execution(* com.project.dao.BookDao.findName(..))")
public void pt(){}
```

3. use @Before("pt()"), @After("pt()"), @Around("pt()") to decorate methods for adding features to old methods.

```java
@Before("pt()")
public void beforeMethod(JoinPoint jp){
  Object[]args=jp.getArgs();
  System.out.println(Arrays.toString(args));
  System.out.println("before advice ...");
  }

@After("pt()")
public void afterMethod(JoinPoint jp){
  Object[]args=jp.getArgs();
  System.out.println(Arrays.toString(args));
  System.out.println("after advice ...");
  }

//ProceedingJoinPoint：专用于环绕通知，是JoinPoint子类，可以实现对原始方法的调用
@Around("pt()")
public Object aroundMethod(ProceedingJoinPoint pjp)throws Throwable{
  Object[]args=pjp.getArgs();
  System.out.println(Arrays.toString(args));
  args[0]=666;
  Object ret=pjp.proceed(args);
  return ret;
  }
```