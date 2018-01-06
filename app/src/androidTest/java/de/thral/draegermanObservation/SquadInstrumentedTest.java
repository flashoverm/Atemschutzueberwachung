package de.thral.draegermanObservation;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.thral.draegermanObservation.business.Draegerman;
import de.thral.draegermanObservation.business.EventType;
import de.thral.draegermanObservation.business.OperatingTime;
import de.thral.draegermanObservation.business.Order;
import de.thral.draegermanObservation.business.Squad;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SquadInstrumentedTest {

    @Test
    public void testStateRegistration(){
        Squad testSquad = new Squad("TestSquad",
                new Draegerman("TestName", "Testname"), 300,
                new Draegerman("TestName", "TestName"), 300,
                OperatingTime.Normal, Order.Firefighting);
        assertEquals(testSquad.getState(), EventType.Register);
    }

    @Test
    public void testStateStart(){
        Squad testSquad = new Squad("TestSquad",
                new Draegerman("TestName", "Testname"), 300,
                new Draegerman("TestName", "TestName"), 300,
                OperatingTime.Normal, Order.Firefighting);
        testSquad.beginOperation();
        assertEquals(testSquad.getState(), EventType.Begin);
    }

    @Test
    public void testStateArrive(){
        Squad testSquad = new Squad("TestSquad",
                new Draegerman("TestName", "Testname"), 300,
                new Draegerman("TestName", "TestName"), 300,
                OperatingTime.Normal, Order.Firefighting);
        testSquad.beginOperation();
        testSquad.arriveTarget(200, 200);
        assertEquals(testSquad.getState(), EventType.Arrive);
    }

    @Test
    public void testPressureArrive(){
        Squad testSquad = new Squad("TestSquad",
                new Draegerman("TestName", "Testname"), 300,
                new Draegerman("TestName", "TestName"), 300,
                OperatingTime.Normal, Order.Firefighting);
        testSquad.beginOperation();
        testSquad.arriveTarget(200, 200);
        assertEquals(testSquad.getLeaderReturnPressure(), 200);
    }

    @Test
    public void testStatePressureState(){
        Squad testSquad = new Squad("TestSquad",
                new Draegerman("TestName", "Testname"), 300,
                new Draegerman("TestName", "TestName"), 300,
                OperatingTime.Normal, Order.Firefighting);
        testSquad.beginOperation();
        testSquad.arriveTarget(200, 200);
        testSquad.pressureOnTime(150, 150);
        assertEquals(testSquad.getState(), EventType.Arrive);
    }

    @Test
    public void testStatePressure(){
        Squad testSquad = new Squad("TestSquad",
                new Draegerman("TestName", "Testname"), 300,
                new Draegerman("TestName", "TestName"), 300,
                OperatingTime.Normal, Order.Firefighting);
        testSquad.beginOperation();
        testSquad.arriveTarget(200, 200);
        testSquad.pressureOnTime(150, 150);
        assertEquals(testSquad.getLastPressureValues(1)[0].getPressureLeader(), 150);
    }

    @Test
    public void testStatePause(){
        Squad testSquad = new Squad("TestSquad",
                new Draegerman("TestName", "Testname"), 300,
                new Draegerman("TestName", "TestName"), 300,
                OperatingTime.Normal, Order.Firefighting);
        testSquad.beginOperation();
        testSquad.pauseOperation();
        assertEquals(testSquad.getState(), EventType.PauseTimer);
    }

    @Test
    public void testStateResume(){
        Squad testSquad = new Squad("TestSquad",
                new Draegerman("TestName", "Testname"), 300,
                new Draegerman("TestName", "TestName"), 300,
                OperatingTime.Normal, Order.Firefighting);
        testSquad.beginOperation();
        testSquad.arriveTarget(200, 200);
        testSquad.pauseOperation();
        testSquad.resumeOperation();
        assertEquals(testSquad.getState(), EventType.Arrive);
    }

    @Test
    public void testStateRetreat(){
        Squad testSquad = new Squad("TestSquad",
                new Draegerman("TestName", "Testname"), 300,
                new Draegerman("TestName", "TestName"), 300,
                OperatingTime.Normal, Order.Firefighting);
        testSquad.beginOperation();
        testSquad.arriveTarget(200, 200);
        testSquad.retreat(100,100);
        assertEquals(testSquad.getState(), EventType.Retreat);
    }

    @Test
    public void testStateEnd(){
        Squad testSquad = new Squad("TestSquad",
                new Draegerman("TestName", "Testname"), 300,
                new Draegerman("TestName", "TestName"), 300,
                OperatingTime.Normal, Order.Firefighting);
        testSquad.beginOperation();
        testSquad.arriveTarget(200, 200);
        testSquad.retreat(100,100);
        testSquad.endOperation(50, 50);
        assertEquals(testSquad.getState(), EventType.End);
    }

}
