package com.src;

import javax.swing.JPanel;

public class Trigger extends Thread{
	Thread t;
	String path;
	Starter kick;
	JPanel runningPanel;
	public Trigger(String path,  JPanel curExePanel) {
		this.path = path;
		kick = new Starter(path);
		this.start();
	}

	public void run(){
		try {
			kick.start();
			//runningPanel.getParent().remove(runningPanel);
		} catch (InterruptedException e) {
			Commons com = kick.getCom();
    		try {
				com.generateReport();
			} catch (Exception e1) {
				e1.printStackTrace();
				com.quitBrowser();
			}
    		com.quitBrowser();
		} catch (Throwable e) {
			e.printStackTrace();
			//runningPanel.getParent().remove(runningPanel);
		} 
	}
	
	public void interruptThread() {
		System.out.println(path);
		this.interrupt();
	}
	
	public void doCleanUp() {
		Commons com = kick.getCom();
		try {
			com.generateReport();
		} catch (Exception e1) {
			e1.printStackTrace();
			com.quitBrowser();
		}
		com.quitBrowser();
	}

}
