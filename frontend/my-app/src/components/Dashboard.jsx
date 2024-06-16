import React, { useEffect, useState } from 'react';
import sensorService from '../services/sensor.service';
import SensorData from './SensorData';
import SensorChart from './SensorChart';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TablePagination from '@mui/material/TablePagination';
import Box from '@mui/material/Box';

const Dashboard = () => {
    const [sensorData, setSensorData] = useState([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await sensorService.getSensorData();
                setSensorData(data);
            } catch (error) {
                console.error('Failed to fetch sensor data', error);
            }
        };

        fetchData();
    }, []);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    return (
        <div>
            <h2>Dashboard</h2>
            <SensorChart data={sensorData} />
            <SensorData data={sensorData} />
            <Paper sx={{ width: "100%", overflow: "hidden" }}>
                <Box height={10} />
                <TableContainer sx={{ maxHeight: 440 }}>
                    <Table stickyHeader aria-label="sticky table">
                        <TableHead>
                            <TableRow>
                                <TableCell align="left" style={{ minWidth: "100px" }}>
                                    Sensor Id
                                </TableCell>
                                <TableCell align="left" style={{ minWidth: "100px" }}>
                                    Date
                                </TableCell>
                                <TableCell align="left" style={{ minWidth: "100px" }}>
                                    Value
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {sensorData
                                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                .map((data) => (
                                    <TableRow hover role="checkbox" tabIndex={-1} key={data.sensor_id}>
                                        <TableCell align="left">{data.sensor_id}</TableCell>
                                        <TableCell align="left">{data.date}</TableCell>
                                        <TableCell align="left">{data.data_value}</TableCell>
                                    </TableRow>
                                ))}
                        </TableBody>
                    </Table>
                </TableContainer>
                <TablePagination
                    rowsPerPageOptions={[10, 25, 100]}
                    component="div"
                    count={sensorData.length}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </Paper>
        </div>
    );
};

export default Dashboard;
