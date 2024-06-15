import React from 'react';
import { Line } from 'react-chartjs-2';

const SensorChart = ({ data }) => {
    const chartData = {
        labels: data.map(sensor => new Date(sensor.date).toLocaleString()),
        datasets: [
            {
                label: 'Temperature (C)',
                data: data.map(sensor => parseFloat(sensor.dataValue.replace('C', ''))),
                fill: false,
                backgroundColor: 'red',
                borderColor: 'rgba(255,0,0,0.2)',
            },
        ],
    };

    return (
        <div>
            <h3>Sensor Chart</h3>
            <Line data={chartData} />
        </div>
    );
};

export default SensorChart;
