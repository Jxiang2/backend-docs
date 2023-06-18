package com.techdocs.di;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class LifecycleDemoBean implements
  InitializingBean, DisposableBean, BeanNameAware, BeanFactoryAware, ApplicationContextAware, BeanPostProcessor {

  public LifecycleDemoBean() {
    System.out.println("## I'm in the LifeCycleBean Constructor ##");
  }

  @Value("${java.specification.version}")
  public void setJavaVer(final String javaVer) {
    System.out.println("## 1 Properties Set. Java Ver: " + javaVer);
  }

  @Override
  public void setBeanName(final String name) {
    System.out.println("## 2 BeanNameAware My Bean Name is: " + name);
  }

  @Override
  public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
    System.out.println("## 3 BeanFactoryAware - Bean Factory has been set");
  }

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    System.out.println("## 4 ApplicationContextAware - Application context has been set");
  }

  @PostConstruct
  public void postConstruct() {
    System.out.println("## 5 postConstruct The Post Construct annotated method has been called");
  }

  @Override
  public void afterPropertiesSet() {
    System.out.println("## 6 afterPropertiesSet Populate Properties The LifeCycleBean has its properties set!");
  }

  @PreDestroy
  public void preDestroy() {
    System.out.println("## 7 The @PreDestroy annotated method has been called");
  }

  @Override
  public void destroy() {
    System.out.println("## 8 DisposableBean.destroy The Lifecycle bean has been terminated");
  }

  @Override
  public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
    System.out.println("## postProcessBeforeInitialization: " + beanName);

    if (bean instanceof final LifecycleDemoBean demoBean) {
      System.out.println("Calling before init");
      demoBean.beforeInit();
    }

    return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
  }

  @Override
  public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
    System.out.println("## postProcessAfterInitialization: " + beanName);

    if (bean instanceof final LifecycleDemoBean demoBean) {
      System.out.println("Calling after init");
      demoBean.afterInit();
    }

    return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
  }

  private void afterInit() {
    System.out.println("## - afterInit - Called by Bean Post Processor");
  }

  private void beforeInit() {
    System.out.println("## - beforeInit - Called by Bean Post Processor");
  }

}