import React from 'react';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, LineElement, CategoryScale, LinearScale, PointElement } from 'chart.js';

ChartJS.register(LineElement, CategoryScale, LinearScale, PointElement);

const SensorChart = ({ data }) => {
    const chartData = {
        labels: data.map((item) => item.date),
        datasets: [
            {
                label: 'Sensor Data',
                data: data.map((item) => parseFloat(item.data_value)),
                borderColor: 'rgba(75,192,192,1)',
                borderWidth: 2,
                fill: false,
            },
        ],
    };

    return (
        <div>
            <h3>Sensor Data Chart</h3>
            <Line data={chartData} />
        </div>
    );
};

export default SensorChart;
