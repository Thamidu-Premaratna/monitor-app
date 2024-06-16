// import React from 'react';
//
// const SensorData = ({ data }) => {
//     return (
//         <div>
//             <h3>Sensor Data</h3>
//             <ul>
//                 {data.map((item) => (
//                     <li key={item.sensor_id}>
//                         {item.date}: {item.data_value}
//                     </li>
//                 ))}
//             </ul>
//         </div>
//     );
// };
//
// export default SensorData;



import React from 'react';

const SensorData = ({ data }) => {
    return (
        <div>
            <h3>Sensor Data</h3>
            <ul>
                {data.map((item) => (
                    <li key={item.sensor_id}>
                        {item.date}: {item.data_value}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default SensorData;
